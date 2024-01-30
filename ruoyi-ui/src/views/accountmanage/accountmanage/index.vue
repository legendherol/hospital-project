<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="医生" prop="docId">
        <el-select v-model="queryParams.docId" placeholder="请选择医生" >
          <el-option
            v-for="dict in doctorList"
            :key="dict.userId"
            :label="dict.username"
            :value="dict.userId">
            <span style="float: left">{{ dict.userId }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.username }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="诊断限额" prop="caseNumer">
        <el-input
          v-model="queryParams.caseNumer"
          placeholder="请输入诊断限额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="科室" prop="offId">
        <el-select v-model="queryParams.offId" placeholder="请选择科室">
          <el-option
            v-for="dict in officeList"
            :key="dict.officeId"
            :label="dict.officeName"
            :value="dict.officeId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排班时间" prop="workTime">
        <el-select v-model="queryParams.workTime" placeholder="请选择排班时间" clearable>
          <el-option
            v-for="dict in dict.type.work_time"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['accountmanage:accountmanage:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['accountmanage:accountmanage:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['accountmanage:accountmanage:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['accountmanage:accountmanage:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountmanageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="医生id" align="center" prop="docId" />
      <el-table-column label="医生名字" align="center" prop="doctor.username" />
      <el-table-column label="诊断限额" align="center" prop="caseNumer" />
      <el-table-column label="排班时间" align="center" prop="workTime">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.work_time" :value="scope.row.workTime"/>
        </template>
      </el-table-column>
      <el-table-column label="科室名字" align="center" prop="office.officeName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['accountmanage:accountmanage:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['accountmanage:accountmanage:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改放号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="医生id" prop="docId">-->
<!--          <el-input v-model="form.docId" placeholder="请输入医生id" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="医生名字" prop="docName">-->
<!--          <el-input v-model="form.docName" placeholder="请输入医生名字" />-->
<!--        </el-form-item>-->
        <el-form-item label="医生" prop="workTime">
          <el-select v-model="form.docId" placeholder="请选择医生" >
            <el-option
              v-for="dict in doctorList"
              :key="dict.id"
              :label="dict.username"
              :value="dict.userId">
            <span style="float: left">{{ dict.userId }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ dict.username }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="诊断限额" prop="caseNumer">
<!--          </el-input-number><el-input v-model="form.caseNumer" placeholder="请输入诊断限额" />-->
          <el-input-number v-model="form.caseNumer" :min="1" :max="20000" label="请输入诊断限额"></el-input-number>
        </el-form-item>
        <el-form-item label="排班时间" prop="workTime">
          <el-select v-model="form.workTime" placeholder="请选择排班时间">
            <el-option
              v-for="dict in dict.type.work_time"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAccountmanage, getAccountmanage, delAccountmanage, addAccountmanage, updateAccountmanage } from "@/api/accountmanage/accountmanage";
import {listOffice} from "@/api/office/office";
import {listDoctor} from "@/api/doctor/doctor";

export default {
  name: "Accountmanage",
  dicts: ['work_time'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      officeList: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      doctorList:[],
      // 放号表格数据
      accountmanageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        docId: null,
        caseNumer: null,
        workTime: null,
        offId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },

  created() {
    this.getOfficeList();
    this.getList();
    this.getDoctorList();
  },
  methods: {

    /** 查询放号列表 */
    getList() {
      this.loading = true;
      listAccountmanage(this.queryParams).then(response => {
        this.accountmanageList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getOfficeList(){
      this.loading = true;
      listOffice(this.queryParams).then(response => {
        this.officeList = response.rows;
        this.loading = false;
      });
    },
    getDoctorList(){
      this.loading = true;
      listDoctor(this.queryParams).then(response => {
        this.doctorList = response.rows;
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
        id: null,
        docId: null,
        caseNumer: null,
        workTime: null,
        offId: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加放号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccountmanage(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改放号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccountmanage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAccountmanage(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除放号编号为"' + ids + '"的数据项？').then(function() {
        return delAccountmanage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('accountmanage/accountmanage/export', {
        ...this.queryParams
      }, `accountmanage_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
