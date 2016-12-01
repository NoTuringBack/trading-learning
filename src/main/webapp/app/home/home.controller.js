(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$rootScope','$scope', 'Principal', 'LoginService', '$state', 'Auth'];

    /* Controller of home, essentially
    manage the authentification process*/
    function HomeController($rootScope, $scope, Principal, LoginService, $state, Auth) {
        var vm = this;

        /*login*/
        vm.username = null;
        vm.password = null;
        vm.rememberMe = true;

        /*authentication*/
        vm.account = null;
        vm.isAuthenticated = null;
        vm.authenticationError = false;

        /*Function to authenticate*/
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.login = login;

        /*Authentification and account function*/
        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        /*For creating a new account*/
        function register() {
            $state.go('register');
        }

        /*Call to login into the app*/
        function login(event) {
            LoginService.login(vm.username, vm.password, vm.rememberMe, vm.authenticationError, Auth);
        }

        /*Lost your password*/
        function requestResetPassword () {
            $state.go('requestReset');
        }

        /*Catch any succes or fail
        in the authentication process*/
        $scope.$on('authenticationSuccess', function() {
            vm.authenticationError = false;
            getAccount();
        });

        $scope.$on('authenticationError', function() {
            vm.authenticationError = true;
        });
    }
})();
