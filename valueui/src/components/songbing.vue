<template>
  <div id="app">
    <el-card class="box-card">
  <div slot="header" class="clearfix">
    <span>MyMuffin</span>
    <el-button style="float: right; padding: 3px 0" type="text" v-on:click="OrderAccepted()">接受订单</el-button>
  </div>
  <div class="text">
    {{"当前任务:"+MyMuffinNowTask }}
  </div>
  <div class="text">{{"Book Revenue:"+MyMuffinNowVar.get('Book Revenue')}}</div>
  <div class="text">{{"Net Income:"+MyMuffinNowVar.get('Net Income')}}</div>
</el-card>
<el-card class="box-card">
  <div slot="header" class="clearfix">
    <span>Customer</span>
    <el-button style="float: right; padding: 3px 0" type="text" v-on:click="startProcess()">下单订购</el-button>
  </div>
  <div class="text">
    {{"当前任务:"+ CustomerNowTask}}
  </div>
  <div class="text">{{"Satisfaction Degree:"+CustomerNowVar.get('Satisfaction Degree')}}</div>
  <div class="text">{{'Payment:' + CustomerNowVar.get('Payment')}}</div>
</el-card>
<el-card class="box-card">
  <div slot="header" class="clearfix">
    <span>Logistics</span>
    <el-button style="float: right; padding: 3px 0" type="text" v-on:click="DeliveryOrderAccepted()">物流运输</el-button>
  </div>
  <!-- <div v-for="o in 4" :key="o" class="text item">
    {{'列表内容 ' + o }}
  </div> -->
  <div class="text">
    {{"当前任务:" + LogisticsNowTask }}
  </div>
  <div class="text">{{"Net income:"+LogisticsNowVar.get('Net Income')}}</div>
</el-card>
<div class="varlist">
<el-table :data="tableData"
              border
              style="width: 100%"
              max-height="300">
      <el-table-column prop="owner"
                       label="参与者"
                       width="150">
      </el-table-column>
      <el-table-column prop="var"
                       label="细化价值"
                       width="150">
      </el-table-column>
      <el-table-column prop="value"
                       label="值">
      </el-table-column>
      <el-table-column prop="waring"
                       label="监控">
      </el-table-column>
    </el-table>
</div>
  </div>
</template>>
<script>
export default {
  data () {
    return {
      show: false,
      owner: 'MyMuffin',
      tableData: [],
      MyMuffinNowTask: 'StartEvent',
      MyMuffinNowVar: new Map(),
      CustomerNowTask: 'StartEvent',
      CustomerNowVar: new Map(),
      LogisticsNowTask: 'StartEvent',
      LogisticsNowVar: new Map()
    }
  },
  created: function () {
    this.MyMuffinNowVar.set('Book Revenue', null)
    this.MyMuffinNowVar.set('Net Income', null)
    this.CustomerNowVar.set('Satisfaction Degree', null)
    this.CustomerNowVar.set('Payment', null)
    this.LogisticsNowVar.set('Net Income', null)
  },
  methods: {
    startProcess () {
      this.CustomerNowTask = 'Make order'
      // this.tableData = [{
      //   owner: this.owner,
      //   var: '王小虎',
      //   value: '上海市普陀区金沙江路 1518 弄',
      //   waring: false
      // }]
    },
    OrderAccepted () {
      this.MyMuffinNowTask = 'Set-up delivery'
      // setTimeout(() => {
      //   this.MyMuffinNowTask = 'Arrangement accepted'
      // }, 1000)
      // setTimeout(() => {
      //   this.MyMuffinNowTask = 'Prepare cooking'
      // }, 2000)
    },
    DeliveryOrderAccepted () {
      this.LogisticsNowTask = 'Arrange for delivery'
      var timeout = 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Arrangement accepted'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Prepare cooking'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Heat oven/Prepare dough/Prepare cooking paper'
      }, timeout)
      timeout += 4000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Cook muffin'
      }, timeout)
      timeout += 4000
      setTimeout(() => {
        this.recook()
        this.MyMuffinNowTask = 'Prepare cooking'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Heat oven/Prepare dough/Prepare cooking paper'
      }, timeout)
      timeout += 4000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Cook muffin'
      }, timeout)
      timeout += 4000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Dispatch muffin'
      }, timeout)
      timeout += 1000
      this.DispatchMuffin(timeout)
    },
    DispatchMuffin (timeout) {
      setTimeout(() => {
        this.LogisticsNowTask = 'Muffin dispatching'
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'DR.DF',
            value: 84,
            waring: 'true'
          })
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'DR.RC',
            value: 54.5,
            waring: 'false'
          })
      }, timeout)
      setTimeout(() => {
        this.openerror()
      }, timeout)
      setTimeout(() => {
        this.LogisticsNowTask = 'Prepare for ordinary delivery'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Deliver muffin'
      }, timeout)
      timeout += 1000
      timeout = this.ChargeForFee(timeout)
      this.MyMuffinNowTask = 'Delivery succeeded'
      setTimeout(() => {
        this.LogisticsNowTask = 'Check muffin'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.CustomerNowTask = 'Muffin received'
        this.tableData.push(
          {
            owner: 'Logistics',
            var: 'LI.FT',
            value: 3,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'Logistics',
            var: 'LI.CC',
            value: '94.8%',
            waring: 'false'
          }
        )
        this.tableData.push(
          {
            owner: 'Customer',
            var: 'CC',
            value: '89.576%',
            waring: 'false'
          }
        )
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.openerror()
        this.CustomerNowVar.set('Satisfaction Degree', '89.576%')
      }, timeout)
      setTimeout(() => {
        this.CustomerNowTask = 'Pay for muffin'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.LogisticsNowTask = 'Accepted'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.LogisticsNowTask = 'Charge for fee'
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.MyMuffinNowTask = 'Pay for logistic'
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'AP',
            value: 132,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'Logistics',
            var: 'AR.Cost',
            value: 50,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'Logistics',
            var: 'AR.ER',
            value: 73,
            waring: 'false'
          }
        )
        this.LogisticsNowVar.set('Net Income', 81)
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.openerror()
      }, timeout)
      timeout += 1000
      setTimeout(() => {
        this.end()
      }, timeout)
    },
    ChargeForFee (timeout) {
      setTimeout(() => {
        this.MyMuffinNowTask = 'Charge for fee'
        this.CustomerNowTask = 'Receive muffin'
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'AR.ER',
            value: 329,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'AR.RC',
            value: 46,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'AR.Cost',
            value: 76,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'MyMuffin',
            var: 'AR.Bonus',
            value: 36,
            waring: 'true'
          }
        )
        this.tableData.push(
          {
            owner: 'Customer',
            var: 'AP',
            value: 323,
            waring: 'true'
          }
        )
        this.MyMuffinNowVar.set('Book Revenue', 389)
        this.MyMuffinNowVar.set('Net Income', 331.86)
        //  sadasdasdasdasda
        this.CustomerNowVar.set('Payment', 323)
      }, timeout)
      return timeout
    },
    openerror () {
      this.$message({
        message: 'Violation of value constraints',
        type: 'warning',
        duration: 1000
      })
    },
    recook () {
      this.$message({
        message: 'Baking failure and baking again',
        type: 'warning',
        duration: 1000
      })
    },
    end () {
      this.$message({
        message: 'End of process',
        type: 'warning',
        duration: 1000
      })
    }
  }
}

</script>>
<style>
.varlist{
    position: relative;
    top: 50px;
    left: 30px;
    width: 700px;
}
.text {
  position: relative;
  left: 0px;
  font-size: 20px；
}
.item {
    margin-bottom: 18px;
  }

.clearfix:before,
.clearfix:after {
display: table;
content: "";
}
.clearfix:after {
clear: both
}
.box-card {
margin:0 30px;
float:left;
width: 300px;
}
</style>
