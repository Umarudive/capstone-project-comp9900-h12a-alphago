package alphago.propertysale.service.impl;

import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.Rab;
import alphago.propertysale.entity.POJO.RabAction;
import alphago.propertysale.mapper.AuctionMapper;
import alphago.propertysale.mapper.RabActionMapper;
import alphago.propertysale.mapper.RabMapper;
import alphago.propertysale.mapper.UserMapper;
import alphago.propertysale.service.RabActionService;
import alphago.propertysale.utils.TimeUtil;
import alphago.propertysale.websocket.BidHistoryPush;
import alphago.propertysale.websocket.BidMsg;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: Implementation of Address service
 */
@Service
@Transactional
public class RabActionServiceImpl extends ServiceImpl<RabActionMapper, RabAction> implements RabActionService {

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private RabActionMapper rabActionMapper;

    @Autowired
    private RabMapper rabMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean bid(RabAction rabAction) {
        Long rabId = rabAction.getRabId();
        Rab rab = rabMapper.selectById(rabId);
        long aid = rab.getAid();
        long uid = rab.getUid();
        Auction auction = auctionMapper.selectById(aid);
        // Check auction status
        if(!auction.getStatus().equals("A")){
            throw new RuntimeException("Auction: " + aid + " is end!");
        }
        // Check highest price
        long bidId = auction.getCurrentBid();
        Rab bid = rabMapper.selectById(bidId);
        if(bid != null && bid.getHighestPrice() >= rabAction.getBidPrice()){
            throw new RuntimeException("The bid price is smaller than current highest price!");
        }
        // add bid
        rabActionMapper.insert(rabAction);
        // update rab's highest price
        rabMapper.update(null, new UpdateWrapper<Rab>()
                                            .eq("rab_id", rabAction.getRabId())
                                            .set("highest_price", rabAction.getBidPrice()));
        // update auction's current bid
        auctionMapper.update(null, new UpdateWrapper<Auction>()
                .eq("aid", aid)
                .set("current_bid", rab.getRabId())
                .set("highest_price", rabAction.getBidPrice())
        );

        boolean overtime = Auction.isExpr(aid);
        if(overtime){
            auctionMapper.update(null, new UpdateWrapper<Auction>()
                .eq("aid", aid)
                .setSql("end_date = date_add(end_date, INTERVAL 2 MINUTE)")
                .set("highest_price", rabAction.getBidPrice())
            );
        }

        BidHistoryPush.bidPush(aid,
                new BidMsg().setUid(uid).setTime(rabAction.getBidTime().toInstant(TimeUtil.getMyZone()).toEpochMilli())
                        .setUsername(userMapper.selectById(uid).getUsername())
                        .setPrice(rabAction.getBidPrice())
                        .setOvertime(overtime));
        return true;
    }
}
