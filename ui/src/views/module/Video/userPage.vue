<template>
  <a-modal
    title="抖音人员"
    :visible="open"
    :footer="null"
    width="95%"
    @cancel="cancel"
    class="userModalBox"
  >
    <a-table
      :loading="loading"
      rowKey="id"
      :columns="columns"
      :data-source="list"
      :pagination="false"
      bordered
      aria-hidden="false"
    >
      <span slot="tags" slot-scope="text, record">
        <a-tag
          v-for="(tag, tagIndex) in (record.tags || '')
            .split(',')
            .filter(Boolean)"
          :key="tagIndex"
          :title="tag"
          color="blue"
        >
          {{ tag }}
        </a-tag>
      </span>
      <span slot="status" slot-scope="text, record">
        <a-tag color="#2db7f5" v-if="record.status == 1"> 正在处理 </a-tag>
        <a-tag color="#ccc" v-else-if="record.status == 0"> 等待处理 </a-tag>
        <a-tag color="#87d068" v-else> 已完成 </a-tag>
      </span>

      <span slot="age" slot-scope="text, record">
        {{ record.real_name ? record.real_name : "未实名" }}
        {{ record.age ? " (" + record.age + ")" : "" }}
      </span>

      <template slot="remark" slot-scope="text, record">
        <!-- 保留官方编辑单元格组件 -->
        <editable-cell
          :ref="`remarkCell_${record.id}`"
          :key="`${record.id}-remark`"
          :text="record.remark"
          @change="(value) => onCellChange(record.id, 'remark', value)"
        />
      </template>
      <span slot="operation" slot-scope="text, record">
        <a
          @click="goDetailPage(record)"
          v-if="record.status == 2 || record.status == 1"
        >
          <a-icon type="right-square" />详情页
        </a>
      </span>
    </a-table>
    <!-- 分页 -->
    <div style="height: 60px">
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="(total) => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </div>
  </a-modal>
</template>

<script>
import { VideoTags, updateVideoTags } from "@/api/module/Video";
const EditableCell = {
  props: { text: String },
  data() {
    return { value: this.text, editable: false };
  },
  render(h) {
    // 编辑状态：输入框 + 确认图标
    if (this.editable) {
      return h(
        "div",
        {
          class: "editable-cell",
          attrs: { "aria-hidden": false },
        },
        [
          h(
            "div",
            {
              class: "editable-cell-input-wrapper",
              attrs: { tabindex: 0 },
            },
            [
              h("a-input", {
                attrs: { value: this.value, "aria-hidden": false },
                on: {
                  change: (e) => {
                    this.value = e.target.value;
                  },
                  pressEnter: () => {
                    this.check();
                  },
                },
              }),
              h("a-icon", {
                attrs: {
                  type: "check",
                  tabindex: -1,
                },
                class: "editable-cell-icon-check",
                on: {
                  click: () => {
                    this.check();
                  },
                },
              }),
            ]
          ),
        ]
      );
    }
    // 非编辑状态：文本 + 编辑图标
    return h(
      "div",
      {
        class: "editable-cell",
        attrs: { "aria-hidden": false },
      },
      [
        h(
          "div",
          {
            class: "editable-cell-text-wrapper",
            attrs: { tabindex: -1 },
          },
          [
            this.value || " ",
            h("a-icon", {
              attrs: {
                type: "edit",
                tabindex: -1,
              },
              class: "editable-cell-icon",
              on: {
                click: () => {
                  this.edit();
                },
              },
            }),
          ]
        ),
      ]
    );
  },
  methods: {
    check() {
      this.editable = false;
      this.$emit("change", this.value);
    },
    edit() {
      this.editable = true;
    },
  },
};

export default {
  name: "userPage",
  props: ["deviceData"],
  components: { EditableCell },
  data() {
    return {
      loading: false,
      open: false,
      list: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {
        pageNum: 1,
        taskId: "",
        pageSize: 10,
      },
      columns: [
        {
          title: "抖音ID",
          dataIndex: "douyinId",
          ellipsis: true,
          width: 140,
          align: "center",
        },
        {
          title: "创建时间",
          dataIndex: "createTime",
          ellipsis: true,
          width: 160,
          align: "center",
        },
        {
          title: "执行的任务",
          scopedSlots: { customRender: "tags" },
          ellipsis: true,
          width: 220,
          align: "left",
        },

        {
          title: "备注",
          scopedSlots: { customRender: "remark" },
          width: 220,
          align: "left",
        },
        {
          title: "昵称",
          dataIndex: "nickname",
          width: 130,
          align: "left",
        },

        {
          title: "实名",
          scopedSlots: { customRender: "age" },
          width: 130,
          align: "left",
        },

        {
          title: "简介",
          dataIndex: "gender",
          align: "left",
        },
        {
          title: "关注数",
          dataIndex: "follow_count",
          align: "left",
          width: 90,
        },
        {
          title: "粉丝数",
          dataIndex: "fans_count",
          align: "left",
          width: 90,
        },
        {
          title: "作品数",
          dataIndex: "works_count",
          align: "left",
          width: 90,
        },

        {
          title: "地区",
          dataIndex: "region",
          align: "left",
        },
        {
          title: "状态",
          scopedSlots: { customRender: "status" },
          width: 110,
          ellipsis: true,
          align: "center",
        },
        {
          title: "操作",
          dataIndex: "operation",
          width: 100,
          scopedSlots: { customRender: "operation" },
          align: "center",
        },
      ],
    };
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    getList() {
      this.loading = true;
      VideoTags(this.queryParam).then((response) => {
        const jsonFieldTemplate = {
          nickname: "",
          real_name: "",
          follow_count: "",
          fans_count: "",
          works_count: "",
          region: "",
          age: "",
        };
        this.list = response.rows.map((item) => {
          // 步骤1：保留原行的所有字段（如 item.id / item.createTime 等）
          const originalData = { ...item };

          // 步骤2：解析 jsonString 字段（容错处理）
          let parsedJson = {};
          try {
            parsedJson = item.account.jsonString
              ? JSON.parse(item.account.jsonString)
              : {};
          } catch (e) {
            console.warn("解析 jsonString 失败：", e, item.account.jsonString);
            parsedJson = {};
          }

          // 步骤3：合并模板与解析后的JSON字段（补全缺失字段）
          const completeJsonData = { ...jsonFieldTemplate, ...parsedJson };

          // 步骤4：合并原行字段 + 补全后的JSON字段（原字段优先级更高，不会被覆盖）
          const finalItem = {
            ...originalData, // 保留原行所有字段（核心：不删除任何原有字段）
            ...completeJsonData, // 合并JSON字段，缺失的用模板空值补全
          };

          // 可选：删除原 jsonString 字段（如果不需要保留）
          // delete finalItem.jsonString;

          return finalItem;
        });

        console.log(this.list);

        this.total = response.total;
        this.loading = false;
      });
    },
    onClose() {
      this.open = false;
    },

    // 取消按钮
    cancel() {
      this.open = false;
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.open = true;
      this.queryParam.taskId = row.id;
      this.getList();
    },

    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize;
      this.getList();
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current;
      this.queryParam.pageSize = pageSize;
      this.getList();
    },
    // 2. 编辑单元格并同步到后端（核心功能）
    // 优化 onCellChange 方法
    async onCellChange(id, dataIndex, value) {
      if (!value) return;
      // 1. 先找到目标行，缓存原始值（关键：避免后续用未定义的record）
      const target = this.list.find((item) => item.id === id);
      if (!target) return; // 无目标行直接退出
      const originValue = target[dataIndex]; // 缓存原始值

      // 2. 本地更新
      target[dataIndex] = value;

      // 3. 调用接口
      try {
        await updateVideoTags({ id, remark: value });
        this.$message?.success("編輯成功");
      } catch (err) {
        this.$message?.error("编辑失败，已回滚");
        // 4. 回滚逻辑：用缓存的原始值重置，而非未定义的record
        target[dataIndex] = originValue; // 先本地回滚
        await this.getList(); // 再刷新列表

        // 可选：重置组件值（用缓存的原始值，而非record）
        const cellRef = this.$refs[`remarkCell_${id}`];
        if (cellRef) {
          cellRef.value = originValue; // 用缓存的原始值重置
          cellRef.editable = false;
        }
      }
    },
    goDetailPage(item) {
      const routeData = this.$router.resolve({
        name: "detailPage",
        query: {
          devId: item.devId,
          douyinId: item.douyinId,
        },
      });

      // 2. 打开新标签页（_blank 表示新标签页）
      window.open(routeData.href, "_blank");
    },
  },
};
</script>

<style >
.userModalBox {
  padding: 0 0 30px 0;
}

.editable-cell {
  position: relative;
  height: 34px;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
  font-size: 16px;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}

.editable-add-btn {
  margin-bottom: 8px;
}
</style>
