<template>

  <a-modal title="详细列表" width="80%" :visible="viewDyVisible" @cancel="onClose" :footer="null">
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="抖音号">
                <a-input v-model="queryParam.douyinId" placeholder="请输入抖音号" allow-clear />
              </a-form-item>
            </a-col>

            <a-col :md="8" :sm="24">
              <span class="table-page-search-submitButtons">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <!-- <div class="table-operations">

        <table-setting :style="{ float: 'right' }" :table-size.sync="tableSize" v-model="columns"
          :refresh-loading="loading" @refresh="getList" />
      </div> -->
      <!-- 数据展示 -->
      <a-table :loading="loading" :size="tableSize" rowKey="deptId" :columns="columns" :data-source="list"
        :pagination="false" :bordered="tableBordered">
      
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <!-- <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record)" v-hasPermi="['system:dept:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-hasPermi="['system:dept:add']" />
          <a @click="$refs.createForm.handleAdd(record)" v-hasPermi="['system:dept:add']">
            <a-icon type="plus" />新增
          </a>
          <a-divider type="vertical" v-hasPermi="['system:dept:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:dept:remove']">
            <a-icon type="delete" />删除
          </a>
        </span> -->
      </a-table>
      <!-- 分页 -->
      <a-pagination class="ant-table-pagination" show-size-changer show-quick-jumper :current="queryParam.pageNum"
        :total="total" :page-size="queryParam.pageSize" :showTotal="total => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange" @change="changeSize" />
    </a-card>
  </a-modal>
</template>

<script>

import { crawl } from '@/api/module/UserSearch'

import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'viewData',
  components: {
  },
  mixins: [tableMixin],
  dicts: [],
  data() {
    return {
      viewDyVisible: false,

      list: [],
      // 部门树选项
      deptOptions: [],
      loading: false,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        douyinId: '',
        devId: '',
      },
      total: 0,

      columns: [
        {
          title: '抖音号',
          dataIndex: 'douyinId',
          align: 'center'
        },
        {
          title: '搜索时间',
          width: 170,
          dataIndex: 'updateTime'
        },

        {
          title: '数据',
          dataIndex: 'jsonString',
          // scopedSlots: { customRender: 'status' },
          align: 'left'
        },

        // {
        //   title: '操作',
        //   dataIndex: 'operation',
        //   width: '30%',
        //   scopedSlots: { customRender: 'operation' },
        //   align: 'center'
        // }
      ]
    }
  },
  filters: {
  },
  created() {
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true
      crawl(this.queryParam).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false
      })
    },
    onClose() {
      this.queryParam.devId = '';
      this.viewDyVisible = false;
      this.list = []
    },
    open(id) {
      this.viewDyVisible = true;
      this.queryParam.devId = id;
      this.queryParam.pageNum = 1;
      this.getList();
    },


    onShowSizeChange(current, pageSize) {
      this.queryParam.pageNum = 1;
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam.pageNum = 1;
      this.queryParam.douyinId = '';

      this.handleQuery()
    },

  }
}
</script>
