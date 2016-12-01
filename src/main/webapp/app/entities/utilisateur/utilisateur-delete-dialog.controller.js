(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('UtilisateurDeleteController',UtilisateurDeleteController);

    UtilisateurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Utilisateur'];

    function UtilisateurDeleteController($uibModalInstance, entity, Utilisateur) {
        var vm = this;

        vm.utilisateur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Utilisateur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
