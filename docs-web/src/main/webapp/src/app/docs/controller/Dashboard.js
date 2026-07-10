'use strict';

/**
 * Dashboard / Workbench controller.
 */
angular.module('docs').controller('Dashboard', function($scope, $state, Restangular) {
  // Initialize stats
  $scope.stats = {
    pendingCount: 0,
    todoReadCount: 0,
    myDocCount: 0,
    doneCount: 0
  };

  $scope.pendingList = [];
  $scope.noticeList = [];
  $scope.recentActivities = [];

  // Navigate to a route
  $scope.goTo = function(route) {
    if (route.indexOf('/') === 0) {
      window.location.hash = '#' + route;
    }
  };

  // Classification label class
  $scope.getClassificationClass = function(type) {
    var map = {
      'RECEIVE': 'label-primary',
      'SEND': 'label-success',
      'MINUTES': 'label-info',
      'NOTICE': 'label-warning',
      'REPORT': 'label-default',
      'REQUEST': 'label-danger',
      'INTERNAL': 'label-primary'
    };
    return map[type] || 'label-default';
  };

  // Urgency label class
  $scope.getUrgencyClass = function(urgency) {
    var map = {
      'NORMAL': 'label-default',
      'URGENT': 'label-warning',
      'EMERGENCY': 'label-danger'
    };
    return map[urgency] || 'label-default';
  };

  // Load dashboard data from API
  // Try to load statistics from the app endpoint
  Restangular.one('app').get().then(function(data) {
    // Use existing app data for doc counts if available
  });

  // Load pending documents
  Restangular.one('document/list').get({ limit: 5 }).then(function(data) {
    if (data && data.documents) {
      $scope.pendingList = data.documents.slice(0, 5);
      $scope.stats.pendingCount = data.total || $scope.pendingList.length;
    }
  });

  // Load user's documents for stats
  Restangular.one('document/list').get({ limit: 1 }).then(function(data) {
    $scope.stats.myDocCount = data.total || 0;
  });

  // Load recent documents as notices
  Restangular.one('document/list').get({ limit: 5, sort_column: 3, asc: false }).then(function(data) {
    if (data && data.documents) {
      $scope.noticeList = data.documents.slice(0, 5);
    }
  });
});
