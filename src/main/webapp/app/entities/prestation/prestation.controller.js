(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('PrestationController', PrestationController);

    PrestationController.$inject = ['$scope', '$state', 'Prestation', 'PrestationSearch'];

    function PrestationController ($scope, $state, Prestation, PrestationSearch) {
        var vm = this;

        vm.prestations = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Prestation.query(function(result) {
                vm.prestations = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PrestationSearch.query({query: vm.searchQuery}, function(result) {
                vm.prestations = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
