(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('ContenuDeleteController',ContenuDeleteController);

    ContenuDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contenu'];

    function ContenuDeleteController($uibModalInstance, entity, Contenu) {
        var vm = this;

        vm.contenu = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Contenu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
