<template>
  <div>
    <div style="margin: 10px 0">
<!--     <el-input style="width: 200px" placeholder="请输入设备编号" suffix-icon="el-icon-search" v-model="idd"></el-input>-->
      <el-input style="width: 200px" placeholder="请输入设备种类" suffix-icon="el-icon-s-tools" class="ml-5" v-model="devicekind"></el-input>
      <el-input style="width: 200px" placeholder="请输入巡检人" suffix-icon="el-icon-s-custom" class="ml-5" v-model="person"></el-input>
      <el-input style="width: 200px" placeholder="请输入设备编码" suffix-icon="el-icon-s-check" class="ml-5" v-model="idd"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增检查 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

<!--      <el-upload action="http://localhost:9090/check/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">-->
<!--        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>-->
<!--      </el-upload>-->

      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="idcheck" label="ID" width="80"></el-table-column>
      <el-table-column prop="person" label="巡检人" width="140"></el-table-column>
      <el-table-column prop="result" label="检查结果" width="120"></el-table-column>
      <el-table-column prop="idd" label="设备编号"></el-table-column>
      <el-table-column prop="devicekind" label="设备种类"></el-table-column>
      <el-table-column prop="checkdate" label="巡检日期"></el-table-column>
      <el-table-column prop="description" label="现场描述"></el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.idcheck)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[ 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="维护号ID">
          <el-input v-model="form.idcheck" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="巡检人">
          <el-input v-model="form.person" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="检查结果">
          <el-select v-model="form.result" placeholder="请选择设备状态">
            <el-option label="正常" value="正常"></el-option>
            <el-option label="异常" value="异常"></el-option>
          </el-select>
<!--          <el-input v-model="form.result" autocomplete="off"></el-input>-->
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.idd" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="设备种类">
          <el-input v-model="form.devicekind" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="巡检日期">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.checkdate" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="现场描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Check",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      idd:"",
      pageSize: 5,
      person: "",
      description:"",
      checkdate: "",
      devicekind:"",
      result: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      headerBg: 'headerBg'
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/check/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          person: this.person,
          checkdate :this.checkdate,
          description :this.description,
          devicekind: this.devicekind,
          idd: this.idd,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
    },
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {  // 收缩
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {   // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    save() {
      this.request.post("/check", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(idcheck) {
      this.request.delete("/ckeck/" + idcheck).then(res => {
        if (res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let idchecks = this.multipleSelection.map(v => v.idcheck)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/ckeck/del/batch", idckecks).then(res => {
        if (res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.idd = ""
      this.person = ""
      this.devicekind = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open("http://122.9.69.149:9090/check/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>