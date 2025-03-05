/**
 * 类型定义
 */

//登录用户信息
export interface UserInfo {
  account?: number;
  name?: string;
  nickname?: string;
  avatar?: string;
  roles?: string[];
  perms?: string[];
}
