<template>
  <div id="app">
    <el-table :data="tableData"
              height="250"
              border
              style="width: 100%">
      <el-table-column prop="date"
                       label="日期"
                       width="180">
      </el-table-column>
      <el-table-column prop="name"
                       label="姓名"
                       width="180">
      </el-table-column>
      <el-table-column prop="address"
                       label="地址">
      </el-table-column>
    </el-table>
  </div>
</template>>
<script>
export default {
  data () {
    return {
      show: false,
      tableData: [{
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-08',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-06',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-07',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }]
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ans = 'IF ' + this.ruleForm.IF + ' THEN ' + this.ruleForm.Topic
          if (this.ruleForm.Policy !== '') {
            this.ans += ' ' + 'policy: ' + this.ruleForm.Policy
          }
          if (this.ruleForm.ID !== '') {
            this.ans += ' ' + 'id: ' + this.ruleForm.ID
          }
          if (this.ruleForm.Name !== '') {
            this.ans += ' ' + 'name: ' + this.ruleForm.Name
          }
          if (this.ruleForm.PointcutType !== '') {
            this.ans += ' ' + 'pointcutType: ' + this.ruleForm.PointcutType
          }
          this.show = true
          //   alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}

</script>>

<style scoped>
.ans {
  color: #000;
  text-align: left;
}
.title {
  text-align: left;
}
</style>
