var app = angular.module('myApp', []);
app.controller('annotationController', function($scope, $http,$rootScope) {
    $http.get("data/annotationData.php")
        .then(function successCallback(response) {
            $scope.sites = response.data.sites;
            $scope.type = response.data.ImplementationType;
            $scope.scenario = response.data.Scenarios;
            $scope.ssptopic = response.data.Ssptopic;
            $scope.coldchaintopic = response.data.Coldchaintopic;
            $scope.pointcutType = response.data.PointcutType;
            $scope.annotation = {Id:"Defaults",Name:"Defaults",implementation:"Defaults",piontcutType:"Defaults",destination:"Defaults",script:"Defaults",InputVariables:"Defaults",
                OutputVariables: "Defaults",scenario:"Defaults",topic:"Defaults"};
        }, function errorCallback(response) {
            $scope.err = "error";
        });
    $scope.reset = function() {
        $scope.annotation = {Id:"Defaults",Name:"Defaults",implementation:"Defaults",piontcutType:"Defaults",destination:"Defaults",script:"Defaults",InputVariables:"Defaults",
            OutputVariables: "Defaults",scenario:"Defaults",topic:"Defaults"};
    };
    $scope.annotaionExtension = {}
    $scope.submit = function() {
        $scope.annotaionExtension = "<" + "l2l:annotation id=" + "\"" + $scope.annotation.Id + "\"" + " " +
            "name=" + "\"" + $scope.annotation.Name + "\"" + " " +
            "implementationType=" + "\"" + $scope.annotation.implementation + "\"" + " " +
            "pointcutType=" + "\"" + $scope.annotation.piontcutType + "\"" + " " +
            "destination=" + "\"" + $scope.annotation.destination + "\"" + " " +
            "script=" + "\"" + $scope.annotation.script + "\"" + " " +
            "topic=" + "\"" + $scope.annotation.topic + "\"" + " " +
            "scenario=" + "\"" + $scope.annotation.scenario + "\"" + " " +
            "/>";//想要把数据以某种格式显示出来
    };

});//无法显示json的读取内容
