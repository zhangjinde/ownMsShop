(function () {
    window.personalInfoJS = window.personalInfoJS || {
            basePath : "http://"+ window.location.host,
            init:function(){
                personalInfoJS.initClick();
            },
            initClick:function(){
                $("#identityAuthId").bind("click",function(){
                    window.location.href = personalInfoJS.basePath + "/identityAuth/toInentityAuthPage.html?auditStatus="+$("#auditStatusId").val();
                    ;
                })
                $("#addressManageId").bind("click",function(){
                    window.location.href =personalInfoJS.basePath + "/userAddress/toManageAddressPage.html?manageAddressJumpType=1&addAddressJumpType=1";
                })
            }
        }
    $(document).ready(function(){
        personalInfoJS.init();
    })
})();