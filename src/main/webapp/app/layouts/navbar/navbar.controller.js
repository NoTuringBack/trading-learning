(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$state', 'Auth', 'Principal', 'ProfileService'];

    function NavbarController ($rootScope, $state, Auth, Principal, ProfileService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });

        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go("home");
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
