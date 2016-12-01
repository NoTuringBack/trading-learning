(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('PrestationDeleteController',PrestationDeleteController);

    PrestationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prestation'];

    function PrestationDeleteController($uibModalInstance, entity, Prestation) {
        var vm = this;

        vm.prestation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prestation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
