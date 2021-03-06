package alphago.propertysale.mapper;

import alphago.propertysale.entity.POJO.Auction;
import alphago.propertysale.entity.POJO.RabAction;
import alphago.propertysale.entity.returnVO.RunningAuctionAddress;
import alphago.propertysale.entity.returnVO.SearchResVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: Data access layer for Auction table in database
 */

public interface AuctionMapper extends BaseMapper<Auction> {

    @Select("SELECT aid, status FROM auction WHERE (status = 'R' OR status = 'A') AND pid = #{pid}")
    Auction getRunningAuctionById(long pid);

    @Select("SELECT * from rab_action r, auction a WHERE a.aid = #{aid} AND a.current_bid = r.rab_id")
    RabAction getAuctionCurrentBid(long aid);

    @Select("SELECT * FROM auction WHERE (status = 'R' OR status = 'A')")
    List<Auction> getAllRunningOrComingAuction();

    @Select("SELECT au.aid, au.status, p.pid, p.bathroom_num , p.bedroom_num , p.garage_num , ad.lat , ad.lng , ad.address ,ad.state ,ad.postcode,ad.suburb \n" +
            "FROM auction au , property p , address ad \n" +
            "WHERE (au.status = 'A' OR au.status = 'R') \n" +
            "AND au.pid = p.pid \n" +
            "AND p.pid = ad.pid ;")
    List<RunningAuctionAddress> getRunningAuctionAddress();

    @Select("SELECT auc.pid, auc.aid, auc.status, auc.minimum_price, auc.highest_price, auc.start_date, auc.end_date, auc.bidder_num," +
            "prop.bathroom_num, prop.bedroom_num, prop.garage_num, prop.area, prop.type  FROM address a, auction auc, property prop ${ew.customSqlSegment}  AND (auc.pid = a.pid)" +
            "and (auc.pid = prop.pid)  and (auc.status = 'R' or auc.status = 'A') ORDER BY auc.aid DESC")
    IPage<SearchResVO> getAllRunningOrComingRes(IPage<SearchResVO> page, @Param(Constants.WRAPPER) Wrapper<SearchResVO> wrapper);

    @Select("SELECT auc.pid, auc.aid, auc.status, auc.minimum_price, auc.highest_price, auc.start_date, auc.end_date, auc.bidder_num," +
            "prop.bathroom_num, prop.bedroom_num, prop.garage_num, prop.area, prop.type  FROM address a, auction auc, property prop ${ew.customSqlSegment}  AND (auc.pid = a.pid)" +
            "and (auc.pid = prop.pid)  and (auc.status = 'R' or auc.status = 'A') ORDER BY auc.highest_price DESC")
    IPage<SearchResVO> getAllRunningOrComingResDESC(IPage<SearchResVO> page, @Param(Constants.WRAPPER) Wrapper<SearchResVO> wrapper);

    @Select("SELECT auc.pid, auc.aid, auc.status, auc.minimum_price, auc.highest_price, auc.start_date, auc.end_date, auc.bidder_num," +
            "prop.bathroom_num, prop.bedroom_num, prop.garage_num, prop.area, prop.type  FROM address a, auction auc, property prop ${ew.customSqlSegment}  AND (auc.pid = a.pid)" +
            "and (auc.pid = prop.pid)  and (auc.status = 'R' or auc.status = 'A') ORDER BY auc.highest_price ASC")
    IPage<SearchResVO> getAllRunningOrComingResASC(IPage<SearchResVO> page, @Param(Constants.WRAPPER) Wrapper<SearchResVO> wrapper);

}
