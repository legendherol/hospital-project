<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="患者id" prop="patientId">
        <el-input
          v-model="queryParams.patientId"
          placeholder="请输入患者id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="患者姓名" prop="patientName">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="挂号医生id" prop="doctorId">
        <el-input
          v-model="queryParams.doctorId"
          placeholder="请输入挂号医生id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="就诊时间" prop="treatmentTime">
        <el-date-picker clearable
                        v-model="queryParams.treatmentTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择就诊时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="挂号医生姓名" prop="doctorName">
        <el-input
          v-model="queryParams.doctorName"
          placeholder="请输入挂号医生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="就诊号" prop="treatmentNum">
        <el-input
          v-model="queryParams.treatmentNum"
          placeholder="请输入就诊号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getNowPatientList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="registrationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="挂号id" align="center" prop="registrationId" />
      <el-table-column label="患者id" align="center" prop="patientId" />
      <el-table-column label="患者姓名" align="center" prop="patientName" />
      <el-table-column label="挂号医生id" align="center" prop="doctorId" />
      <el-table-column label="就诊时间" align="center" prop="treatmentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.treatmentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="挂号医生姓名" align="center" prop="doctorName" />
      <el-table-column label="就诊号" align="center" prop="treatmentNum" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getNowPatientList"
    />



  </div>
</template>

<script>
import {listRegistration, delRegistration, listNowBook} from "@/api/book/registration";

export default {
  name: "Registration",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预约表格数据
      registrationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientId: null,
        patientName: null,
        doctorId: null,
        treatmentTime: null,
        doctorName: null,
        treatmentNum: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientId: [
          { required: true, message: "患者id不能为空", trigger: "blur" }
        ],
        patientName: [
          { required: true, message: "患者姓名不能为空", trigger: "blur" }
        ],
        doctorId: [
          { required: true, message: "挂号医生id不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "挂号时间不能为空", trigger: "blur" }
        ],
        doctorName: [
          { required: true, message: "挂号医生姓名不能为空", trigger: "blur" }
        ],
        treatmentNum: [
          { required: true, message: "就诊号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getNowPatientList();
  },
  methods: {
    /** 查询预约列表 */
    getList() {
      this.loading = true;
      listRegistration(this.queryParams).then(response => {
        this.registrationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getNowPatientList(){
      this.loading = true;
      listNowBook(this.queryParams).then(response => {
        this.registrationList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        registrationId: null,
        patientId: null,
        patientName: null,
        doctorId: null,
        createTime: null,
        treatmentTime: null,
        doctorName: null,
        treatmentNum: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getNowPatientList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.registrationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加预约";
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const registrationIds = row.registrationId || this.ids;
      this.$modal.confirm('是否确认删除预约编号为"' + registrationIds + '"的数据项？').then(function() {
        return delRegistration(registrationIds);
      }).then(() => {
        this.getNowPatientList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('book/registration/export', {
        ...this.queryParams
      }, `registration_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
