(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('UtilisateurController', UtilisateurController);

    UtilisateurController.$inject = ['$scope', '$state', 'Utilisateur', 'UtilisateurSearch'];

    function UtilisateurController ($scope, $state, Utilisateur, UtilisateurSearch) {
        var vm = this;

        vm.utilisateurs = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Utilisateur.utilisateur().query(function(result) {
                vm.utilisateurs = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            UtilisateurSearch.query({query: vm.searchQuery}, function(result) {
                vm.utilisateurs = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
