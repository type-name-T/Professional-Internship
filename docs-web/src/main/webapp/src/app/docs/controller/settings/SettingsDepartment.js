'use strict';

angular.module('docs').controller('SettingsDepartment', function($scope, $http, Restangular) {
  $scope.departments = [];
  $scope.deptUsers = [];
  $scope.newDept = {};
  $scope.showAddForm = false;
  $scope.showAddUser = false;

  // Load departments
  function loadDepartments() {
    Restangular.all('department').getList().then(function(data) {
      $scope.departments = data.departments || [];
      // Load user counts for each department
      angular.forEach($scope.departments, function(d) {
        Restangular.one('user/department/users').get({ department: d.id }).then(function(r) {
          d.userCount = (r.users || []).length;
        });
      });
    });
  }
  loadDepartments();

  // Add department
  $scope.addDepartment = function() {
    if (!$scope.newDept.name) return alert('请输入部门名称');
    Restangular.all('department').post($scope.newDept, {}, {'Content-Type': 'application/x-www-form-urlencoded'}).then(function() {
      alert('部门创建成功');
      $scope.showAddForm = false;
      $scope.newDept = {};
      loadDepartments();
    });
  };

  // Delete department
  $scope.deleteDepartment = function(id) {
    if (!confirm('确定删除此部门？')) return;
    Restangular.one('department', id).remove().then(function() {
      loadDepartments();
    });
  };

  // Select department and load users
  $scope.selectDept = function(dept) {
    $scope.selectedDept = dept;
    $scope.showAddUser = false;
    Restangular.one('user/department/users').get({ department: dept.id }).then(function(r) {
      $scope.deptUsers = r.users || [];
    });
  };

  // Add user to department
  $scope.addUserToDept = function() {
    if (!$scope.addUserName) return;
    $http.post('../api/user/' + $scope.addUserName + '/department',
      'department_id=' + encodeURIComponent($scope.selectedDept.id),
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    ).then(function() {
      alert('用户已添加到部门');
      $scope.showAddUser = false;
      $scope.addUserName = '';
      $scope.selectDept($scope.selectedDept);
      loadDepartments();
    }, function() {
      alert('用户不存在或操作失败');
    });
  };

  // Remove user from department
  $scope.removeUserFromDept = function(username) {
    if (!confirm('确定移出此用户？')) return;
    $http.post('../api/user/' + username + '/department',
      'department_id=',
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    ).then(function() {
      $scope.selectDept($scope.selectedDept);
      loadDepartments();
    });
  };
});
