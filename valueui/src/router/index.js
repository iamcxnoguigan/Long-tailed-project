import Vue from 'vue'
import Router from 'vue-router'
import cuntao from '@/components/cuntao'
import HelloWorld from '@/components/HelloWorld'
import songbing from '@/components/songbing'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/cuntao',
      name: 'cuntao',
      component: cuntao
    },
    {
      path: '/songbing',
      name: 'songbing',
      component: songbing
    }
  ]
})
