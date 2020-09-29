import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
<<<<<<< Updated upstream
// import Register from '../views/Register.vue'
// import Resetpassword from "../views/Resetpassword";
=======
import Register from '../views/Register.vue'
import Resetpassword from "../views/Resetpassword";
>>>>>>> Stashed changes

Vue.use(VueRouter);

const routes = [{
  path: '/',
  name: 'home',
  component: Home
},
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
<<<<<<< Updated upstream
  // {
  //   path: '/register',
  //   name: 'register',
  //   component: Register,
  // }
  //   ,
  // {
  //   path: '/reset',
  //   name: 'reset',
  //   component: Resetpassword,
  // }
=======
  {
    path: '/register',
    name: 'register',
    component: Register,
  }
    ,
  {
    path: '/reset',
    name: 'reset',
    component: Resetpassword,
  }
>>>>>>> Stashed changes
];

const router = new VueRouter({
  mode: 'history',
  routes
});

export default router
