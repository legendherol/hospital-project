import request from '@/utils/request'

// 查询放号列表
export function listAccountmanage(query) {
  return request({
    url: '/accountmanage/accountmanage/list',
    method: 'get',
    params: query
  })
}

// 查询放号详细
export function getAccountmanage(id) {
  return request({
    url: '/accountmanage/accountmanage/' + id,
    method: 'get'
  })
}

// 新增放号
export function addAccountmanage(data) {
  return request({
    url: '/accountmanage/accountmanage',
    method: 'post',
    data: data
  })
}

// 修改放号
export function updateAccountmanage(data) {
  return request({
    url: '/accountmanage/accountmanage',
    method: 'put',
    data: data
  })
}

// 删除放号
export function delAccountmanage(id) {
  return request({
    url: '/accountmanage/accountmanage/' + id,
    method: 'delete'
  })
}
