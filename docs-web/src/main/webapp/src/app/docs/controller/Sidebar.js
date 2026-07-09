'use strict';

/**
 * Sidebar navigation controller.
 */
angular.module('docs').controller('Sidebar', function($scope, $state, $rootScope) {
  // Track which menu groups are open
  var openGroups = { work: true, personal: false, admin: false };

  // Auto-open groups based on current state
  $scope.$watch(function() { return $state.current.name; }, function(stateName) {
    if (!stateName) return;
    if (stateName.indexOf('document') === 0 || stateName.indexOf('dashboard') === 0) {
      openGroups.work = true;
    }
    if (stateName.indexOf('settings') === 0 && stateName !== 'settings.account') {
      openGroups.admin = true;
    }
    if (stateName.indexOf('settings.account') === 0 || stateName.indexOf('settings.security') === 0 ||
        stateName.indexOf('settings.session') === 0) {
      openGroups.personal = true;
    }
  });

  $scope.toggleGroup = function(group) {
    openGroups[group] = !openGroups[group];
  };

  $scope.isGroupOpen = function(group) {
    return openGroups[group];
  };
});
