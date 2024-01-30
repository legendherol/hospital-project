<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药品名称" prop="medicineName">
        <el-input
          v-model="queryParams.medicineName"
          placeholder="请输入药品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="药品规格" prop="medicineNorm">
        <el-input
          v-model="queryParams.medicineNorm"
          placeholder="请输入药品规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产厂家" prop="productVender">
        <el-input
          v-model="queryParams.productVender"
          placeholder="请输入生产厂家"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用法" prop="userWay">
        <el-input
          v-model="queryParams.userWay"
          placeholder="请输入用法"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="药品成分" prop="medicineMaterial">
        <el-input
          v-model="queryParams.medicineMaterial"
          placeholder="请输入药品成分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="药品代码" prop="medicineCode">
        <el-input
          v-model="queryParams.medicineCode"
          placeholder="请输入药品代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input
          v-model="queryParams.stock"
          placeholder="请输入库存"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['medicine:medicine:add']"
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
          v-hasPermi="['medicine:medicine:edit']"
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
          v-hasPermi="['medicine:medicine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['medicine:medicine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="药品信息id" align="center" prop="medicineMsgId" />
      <el-table-column label="药品名称" align="center" prop="medicineName" />
      <el-table-column label="药品规格" align="center" prop="medicineNorm" />
      <el-table-column label="生产厂家" align="center" prop="productVender" />
      <el-table-column label="用法" align="center" prop="userWay" />
      <el-table-column label="药品成分" align="center" prop="medicineMaterial" />
      <el-table-column label="药品代码" align="center" prop="medicineCode" />
      <el-table-column label="库存" align="center" prop="stock" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['medicine:medicine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['medicine:medicine:remove']"
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

    <!-- 添加或修改药品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="药品名称" prop="medicineName">
          <el-input v-model="form.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="药品规格" prop="medicineNorm">
          <el-input v-model="form.medicineNorm" placeholder="请输入药品规格" />
        </el-form-item>
        <el-form-item label="生产厂家" prop="productVender">
          <el-input v-model="form.productVender" placeholder="请输入生产厂家" />
        </el-form-item>
        <el-form-item label="用法" prop="userWay">
          <el-input v-model="form.userWay" placeholder="请输入用法" />
        </el-form-item>
        <el-form-item label="药品成分" prop="medicineMaterial">
          <el-input v-model="form.medicineMaterial" placeholder="请输入药品成分" />
        </el-form-item>
        <el-form-item label="药品代码" prop="medicineCode">
          <el-input v-model="form.medicineCode" placeholder="请输入药品代码" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model="form.stock" placeholder="请输入库存" />
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
import { listMedicine, getMedicine, delMedicine, addMedicine, updateMedicine } from "@/api/medicine/medicine";

export default {
  name: "Medicine",
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
      // 药品表格数据
      medicineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicineName: null,
        medicineNorm: null,
        productVender: null,
        userWay: null,
        medicineMaterial: null,
        medicineCode: null,
        stock: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询药品列表 */
    getList() {
      this.loading = true;
      listMedicine(this.queryParams).then(response => {
        this.medicineList = response.rows;
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
        medicineMsgId: null,
        medicineName: null,
        medicineNorm: null,
        productVender: null,
        userWay: null,
        medicineMaterial: null,
        medicineCode: null,
        stock: null
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
      this.ids = selection.map(item => item.medicineMsgId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加药品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const medicineMsgId = row.medicineMsgId || this.ids
      getMedicine(medicineMsgId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改药品";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.medicineMsgId != null) {
            updateMedicine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMedicine(this.form).then(response => {
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
      const medicineMsgIds = row.medicineMsgId || this.ids;
      this.$modal.confirm('是否确认删除药品编号为"' + medicineMsgIds + '"的数据项？').then(function() {
        return delMedicine(medicineMsgIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('medicine/medicine/export', {
        ...this.queryParams
      }, `medicine_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
