<template>
  <div style="background-color:white;min-height:800px">
    <div style="width:100%;background-color:#484848; overflow: hidden">
      <span class="demonstration"
            style="float:left;padding-top:10px;color:white;margin-left:1%;height:42px;">
        Process Monitor
      </span>
      <span class="demonstration"
            style="float:left;padding:5px;color:white;margin-left:2%;width:15%">
        <!-- <el-input
                        placeholder="请输入"
                        icon="search"
                        v-model="searchCriteria"
                        :on-icon-click="handleIconClick">
                </el-input> -->
      </span>
      <span class="demonstration"
            style="float:right;padding-top:10px;margin-right:1%">
        <el-dropdown trigger="click">
          <span class="el-dropdown-link"
                style="color:white">
            admin<i class="el-icon-caret-bottom el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>Personal information</el-dropdown-item>
            <el-dropdown-item>Logout</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </span>
    </div>

    <div style="margin-top:5px">
      <el-row :gutter="10">
        <el-col :xs="4"
                :sm="4"
                :md="4"
                :lg="4">
          <div>
            <el-radio-group v-model="isCollapse"
                            style="margin-bottom: 0px;">
              <el-radio-button :label="false">expand</el-radio-button>
              <el-radio-button :label="true">collapse</el-radio-button>
            </el-radio-group>
            <el-menu default-active="1"
                     class="el-menu-vertical-demo"
                     style="min-height:800px;"
                     @open="handleOpen"
                     @close="handleClose"
                     :collapse="isCollapse"
                     @select="handleSelect">
              <el-menu-item index="1"><i class="el-icon-setting"></i><span>Annotation Editor</span></el-menu-item>
              <el-submenu index="2">
                <template slot="title">
                  <i class="el-icon-menu"></i>
                  <span slot="title">Monitor</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="2-1">IOT-Enable</el-menu-item>
                  <el-menu-item index="2-2">ColdChain</el-menu-item>
                  <el-menu-item index="2-3">SSP</el-menu-item>
                  <el-menu-item index="2-4">MyMuffin</el-menu-item>
                  <el-menu-item index="2-5">cuntao2.0</el-menu-item>
                </el-menu-item-group>
              </el-submenu>
              <el-menu-item index="3"><i class="el-icon-setting"></i><span>setting</span></el-menu-item>
            </el-menu>
          </div>
        </el-col>
        <el-col :xs="20"
                :sm="20"
                :md="20"
                :lg="20">
          <!-- <div>
                        <div style="border: 1px solid #A6A6A6; border-radius:6px; padding:5px; margin:2px; background-color: white">
                            <el-breadcrumb separator="/">
                                <el-breadcrumb-item v-for="item in breadcrumbItems">{{item}}</el-breadcrumb-item>
                            </el-breadcrumb>
                        </div>
                    </div> -->
          <div style="margin-top:10px">
            <router-view></router-view>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
export default {
  data () {
    return {
      // searchCriteria: '',
      breadcrumbItems: ['Annotation Editor'],
      isCollapse: true
    }
  },

  methods: {
    handleIconClick (ev) {
      console.log(ev)
    },
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    handleSelect (key, keyPath) {
      switch (key) {
        case '1':
          this.$router.push('/annotation')
          this.breadcrumbItems = ['Annotation Editor']
          break
        case '2-1':
          this.$router.push('/Iot')
          this.breadcrumbItems = ['IOT-Enable']
          break
        case '2-2':
          this.$router.push('/ColdChain')
          this.breadcrumbItems = ['ColdChain']
          break
        case '2-3':
          this.$router.push('/SSP')
          this.breadcrumbItems = ['导航三']
          break
        case '2-4':
          this.$router.push('/songbing')
          this.breadcrumbItems = ['松饼']
          break
        case '2-5':
          this.$router.push('/cuntao')
          this.breadcrumbItems = ['村淘']
          break
      }
    }

  }
}
</script>

<style>
@import "https://unpkg.com/element-ui/lib/theme-chalk/index.css";
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.info {
  float: right;
  padding-right: 300px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
}
</style>
