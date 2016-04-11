/**
 * 确认提现
 */
function withdraw(userId){
    if (userId == undefined){
        alert("暂无提现金额");
        return;
    }
    var extractableFee = $("#extractableFee").val();
    var inputAccount = $("#inputAccount").val();
    if (extractableFee == 0){
        alert("暂无可提现金额");
        return;
    }
    if (inputAccount == ""){
        alert("请输入提现金额");
        return;
    }
    if (inputAccount > extractableFee){
        alert("输入金额大于可提现金额");
        return;
    }
    $.ajax({
        type:"POST",
        async:true,
        url : basepath+"/withdraw/confirmWithdraw.do",
        data:{userId:userId,inputAccount:inputAccount},
        dataType:"Json",
        success:function(data){
            if(data.isTrue == "false"){
                alert(data.message);
            }else {
                window.location.href = basepath + data.resUrl;
            }
        },
        error: function(){
            //请求出错处理
            alert("请求出错，请稍后再试");
        }
    });
}