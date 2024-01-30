import request from '@/utils/request'

// 查询科室列表
export function listOffice(query) {
  return request({
    url: '/office/office/list',
    method: 'get',
    params: query
  })
}

// 查询科室详细
export function getOffice(officeId) {
  return request({
    url: '/office/office/' + officeId,
    method: 'get'
  })
}

// 新增科室
export function addOffice(data) {
  return request({
    url: '/office/office',
    method: 'post',
    data: data
  })
}

// 修改科室
export function updateOffice(data) {
  return request({
    url: '/office/office',
    method: 'put',
    data: data
  })
}

// 删除科室
export function delOffice(officeId) {
  return request({
    url: '/office/office/' + officeId,
    method: 'delete'
  })
}
