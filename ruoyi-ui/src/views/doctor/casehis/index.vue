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
      <el-form-item label="诊断医生id" prop="createUserId">
        <el-input
          v-model="queryParams.createUserId"
          placeholder="请输入诊断医生id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主诉" prop="chiefComplaint">
        <el-input
          v-model="queryParams.chiefComplaint"
          placeholder="请输入主诉"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入医生用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="既往史" prop="previousHistory">
        <el-input
          v-model="queryParams.previousHistory"
          placeholder="请输入既往史"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="过敏史" prop="allergyHistory">
        <el-input
          v-model="queryParams.allergyHistory"
          placeholder="请输入过敏史"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="诊断结果" prop="caseResult">
        <el-input
          v-model="queryParams.caseResult"
          placeholder="请输入诊断结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="诊断时间" prop="caseTime">
        <el-date-picker clearable
                        v-model="queryParams.caseTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择诊断时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="处方" prop="prescriptionDrug">
        <el-input
          v-model="queryParams.prescriptionDrug"
          placeholder="请输入处方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医嘱" prop="caseAdvice">
        <el-input
          v-model="queryParams.caseAdvice"
          placeholder="请输入医嘱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="患者姓名" prop="patienName">
        <el-input
          v-model="queryParams.patienName"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="医生姓名" prop="doctorName">
        <el-input
          v-model="queryParams.doctorName"
          placeholder="请输入医生姓名"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="caseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="诊断id" align="center" prop="caseHistoryId" />
      <el-table-column label="患者id" align="center" prop="patientId" />
      <el-table-column label="诊断医生id" align="center" prop="createUserId" />
      <el-table-column label="主诉" align="center" prop="chiefComplaint" />
      <el-table-column label="医生用户id" align="center" prop="userId" />
      <el-table-column label="既往史" align="center" prop="previousHistory" />
      <el-table-column label="过敏史" align="center" prop="allergyHistory" />
      <el-table-column label="诊断结果" align="center" prop="caseResult" />
      <el-table-column label="诊断时间" align="center" prop="caseTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.caseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处方" align="center" prop="prescriptionDrug" />
      <el-table-column label="医嘱" align="center" prop="caseAdvice" />
      <el-table-column label="患者姓名" align="center" prop="patienName" />
      <el-table-column label="医生姓名" align="center" prop="doctorName" />
      <el-table-column label="就诊号" align="center" prop="treatmentNum" />
      <el-table-column label="备注" align="center" prop="remark" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import {listCase, getCase, delCase, addCase, updateCase, listDocCase} from "@/api/case/case";

export default {
  name: "Case",
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
      // 诊断表格数据
      caseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        patientId: null,
        createUserId: null,
        chiefComplaint: null,
        userId: null,
        previousHistory: null,
        allergyHistory: null,
        caseResult: null,
        caseTime: null,
        prescriptionDrug: null,
        caseAdvice: null,
        patienName: null,
        doctorName: null,
        treatmentNum: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        patientId: [
          { required: true, message: "患者id不能为空", trigger: "blur" }
        ],
        chiefComplaint: [
          { required: true, message: "主诉不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "医生用户id不能为空", trigger: "blur" }
        ],
        previousHistory: [
          { required: true, message: "既往史不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询诊断列表 */
    getList() {
      this.loading = true;
      listDocCase(this.queryParams).then(response => {
        this.caseList = response.rows;
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
        caseHistoryId: null,
        patientId: null,
        createUserId: null,
        chiefComplaint: null,
        userId: null,
        previousHistory: null,
        allergyHistory: null,
        caseResult: null,
        caseTime: null,
        prescriptionDrug: null,
        caseAdvice: null,
        patienName: null,
        doctorName: null,
        treatmentNum: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.caseHistoryId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加诊断";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const caseHistoryId = row.caseHistoryId || this.ids
      getCase(caseHistoryId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改诊断";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.caseHistoryId != null) {
            updateCase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCase(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const caseHistoryIds = row.caseHistoryId || this.ids;
      this.$modal.confirm('是否确认删除诊断编号为"' + caseHistoryIds + '"的数据项？').then(function() {
        return delCase(caseHistoryIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('case/case/export', {
        ...this.queryParams
      }, `case_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
