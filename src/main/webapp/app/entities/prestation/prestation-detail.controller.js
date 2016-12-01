(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('PrestationDetailController', PrestationDetailController);

    PrestationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prestation', 'Utilisateur'];

    function PrestationDetailController($scope, $rootScope, $stateParams, previousState, entity, Prestation, Utilisateur) {
        var vm = this;

        vm.prestation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tradinglearningApp:prestationUpdate', function(event, result) {
            vm.prestation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
