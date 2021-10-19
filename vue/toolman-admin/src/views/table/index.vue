<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template #scope>
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column label="Title">
        <template #scope>
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="Author" width="110" align="center">
        <template #scope>
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Pageviews" width="110" align="center">
        <template #scope>
          {{ scope.row.pageviews }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="created_at" label="Display_time" width="200">
        <template #scope>
          <i class="el-icon-time" />
          <span>{{ scope.row.display_time }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      list: null,
      listLoading: false
    }
  },
}
</script>
