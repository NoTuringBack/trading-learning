(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('ContenuDetailController', ContenuDetailController);

    ContenuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Contenu', 'Utilisateur'];

    function ContenuDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Contenu, Utilisateur) {
        var vm = this;

        vm.contenu = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('tradinglearningApp:contenuUpdate', function(event, result) {
            vm.contenu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
