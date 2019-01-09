<%@ page pageEncoding="UTF-8"%>
<div class="accordion" fillSpace="sidebar">
    <shiro:hasPermission name="user">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>会员管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/userList.html">
                    <li><a href="ssadmin/userList.html" target="navTab"
                           rel="userList">会员列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userAuditList.html">
                    <li><a href="ssadmin/userAuditList1.html" target="navTab"
                           rel="userAuditList1">实名认证</a></li>
                    <%--<li><a href="ssadmin/userAuditList.html" target="navTab"--%>
                           <%--rel="userAuditList">待审核(银行卡复印件)列表</a></li>--%>
                    <li><a href="ssadmin/userOTCAuditList.html" target="navTab"
                           rel="userOTCAuditList">待审核(商家OTC认证)列表</a></li>
                    <!-- <li><a href="ssadmin/userAuditList2.html" target="navTab"
                    rel="userAuditList2">待审核(视频认证)列表</a></li>    -->
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/logList.html">
                    <li><a href="ssadmin/logList.html" target="navTab"
                           rel="logList">会员操作日志列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/introlinfoList.html">
                    <li><a href="ssadmin/introlinfoList.html" target="navTab"
                           rel="introlinfoList">推广收益列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/bankinfoWithdrawList.html">
                    <li><a href="ssadmin/bankinfoWithdrawList.html"
                           target="navTab" rel="bankinfoWithdrawList">会员银行帐户列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualaddressWithdrawList.html">
                    <li><a href="ssadmin/virtualaddressWithdrawList.html"
                           target="navTab" rel="virtualaddressWithdrawList">会员虚拟币提现地址列表</a>
                    </li>
                    <li><a href="ssadmin/virtualaddressList.html" target="navTab"
                           rel="virtualaddressList">会员虚拟币充值地址列表</a>
                    </li>
                </shiro:hasPermission>
                <!-- <shiro:hasPermission name="ssadmin/assetList.html">
                    <li><a href="ssadmin/assetList.html" target="navTab"
                        rel="assetList">会员资产记录列表</a></li>
                </shiro:hasPermission> -->
                <shiro:hasPermission name="ssadmin/subscriptionList1.html">
                    <li><a href="ssadmin/subscriptionList1.html" target="navTab"
                           rel="subscriptionList1">众筹列表</a></li>
                    <!--        <li><a href="ssadmin/subscriptionList.html" target="navTab"
                    rel="subscriptionList">兑换列表</a>
                    </li> -->
                </shiro:hasPermission>
                <!-- <shiro:hasPermission name="ssadmin/sharePlanList.html">
                    <li><a href="ssadmin/sharePlanList.html" target="navTab"
                        rel="sharePlanList">分钱计划列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/handingSharePlanList.html">
                    <li><a href="ssadmin/handingSharePlanList.html"
                        target="navTab" rel="handingSharePlanList">分币计划列表</a>
                    </li>
                </shiro:hasPermission> -->
                <shiro:hasPermission name="ssadmin/entrustList.html">
                    <li><a href="ssadmin/entrustList.html" target="navTab"
                           rel="entrustList">用户/机器人委托交易列表</a>
                    </li>
                    <!-- <li><a href="ssadmin/tradehistoryList.html" target="navTab"
                    rel="tradehistoryList">历史收盘价列表</a>
                    </li> -->
                </shiro:hasPermission>
                <!-- <shiro:hasPermission name="ssadmin/ctctradeList.html">
                    <li><a href="ssadmin/ctctradeList.html" target="navTab"
                        rel="ctctradeList">c2c订单列表</a>
                    </li>
                </shiro:hasPermission> -->
                <shiro:hasPermission name="ssadmin/ctctradeList.html">
                    <li><a href="ssadmin/otcOrdersList.html" target="navTab"
                           rel="otcOrderList">OTC广告记录列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/ctctradeList.html">
                    <li><a href="ssadmin/userotcOrderList.html" target="navTab"
                           rel="userotcOrderList">OTC订单列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/ctctradeList.html">
                    <li><a href="ssadmin/otcOrdersCheckList.html" target="navTab"
                           rel="otcOrderCheckList">审核广告信息表</a>
                    </li>
                </shiro:hasPermission>

            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="article">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>资讯管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/articleList.html">
                    <li><a href="ssadmin/articleList.html" target="navTab"
                           rel="articleList">资讯列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/articleTypeList.html">
                    <li><a href="ssadmin/articleTypeList.html" target="navTab"
                           rel="articleTypeList">资讯类型</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="capital">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>虚拟币操作管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/virtualCoinTypeList.html">
                    <li><a href="ssadmin/virtualCoinTypeList.html" target="navTab"
                           rel="virtualCoinTypeList">虚拟币类型列表</a>
                    </li>
                    <li><a href="ssadmin/tradeMappingList.html" target="navTab"
                           rel="tradeMappingList">法币类型匹配列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/walletAddressList.html">
                    <li><a href="ssadmin/walletAddressList.html" target="navTab"
                           rel="walletAddressList" title="虚拟币可用地址列表">虚拟币可用地址列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCaptualoperationList.html">
                    <li><a href="ssadmin/virtualCaptualoperationList.html"
                           target="navTab" rel="virtualCaptualoperationList">虚拟币操作总表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalInList.html">
                    <li><a href="ssadmin/virtualCapitalInList.html"
                           target="navTab" rel="virtualCapitalInList">虚拟币充值列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalOutList.html">
                    <li><a href="ssadmin/virtualCapitalOutList.html"
                           target="navTab" rel="virtualCapitalOutList">虚拟币提现列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalOutSucList.html">
                    <li><a href="ssadmin/virtualCapitalOutSucList.html"
                           target="navTab" rel="virtualCapitalOutSucList">虚拟币成功提现列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualwalletList.html">
                    <li><a href="ssadmin/virtualwalletList.html" target="navTab"
                           rel="virtualwalletList">会员虚拟币列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualwalletList.html">
                    <li><a href="ssadmin/virtualwalletChangeList.html" target="navTab"
                           rel="virtualwalletChangeList">会员虚拟币变更记录表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
                    <li><a href="ssadmin/virtualoperationlogList.html"
                           target="navTab" rel="virtualoperationlogList">虚拟币手工充值列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalOutSucList.html">
                    <li><a href="ssadmin/transferCoinList.html" target="navTab"
                           rel="virtualTransferSucList">虚拟币转入记录表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
                    <li><a href="ssadmin/withDrawalList.html"
                           target="navTab" rel="withDrawalList">挂单撤销</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
                    <li><a href="ssadmin/withDrawalCancelList.html"
                           target="navTab" rel="withDrawalList">挂单撤销记录</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
                    <li><a href="ssadmin/virtualTransferList.html" target="navTab"
                           rel="virtualTransferList">待审核优生活划转</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalOutSucList.html">
                    <li><a href="ssadmin/virtualTransferSucList.html"
                           target="navTab" rel="virtualTransferSucList">已审核优生活划转</a></li>
                </shiro:hasPermission>
                <!--        <shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
                     <li><a href="ssadmin/virtualFtransformList.html"
                        target="navTab" rel="virtualFtransformList">待审核封神榜划转</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/virtualCapitalOutSucList.html">
                    <li><a href="ssadmin/virtualFtransformSucList.html"
                        target="navTab" rel="virtualFtransformSucList">已审核封神榜划转</a>
                    </li>
                </shiro:hasPermission> -->
                <!--<shiro:hasPermission name="ssadmin/transportlogList.html">
                    <li><a href="ssadmin/transportlogList.html" target="navTab"
                        rel="transportlogList">会员转帐记录列表</a></li>
                </shiro:hasPermission> -->
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="cnycapital">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>人民币操作管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/capitaloperationList.html">
                    <li><a href="ssadmin/capitaloperationList.html"
                           target="navTab" rel="capitaloperationList">人民币操作总表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalInSucList.html">
                    <li><a href="ssadmin/capitalInSucList.html" target="navTab"
                           rel="capitalInSucList">成功充值人民币列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalOutSucList.html">
                    <li><a href="ssadmin/capitalOutSucList.html" target="navTab"
                           rel="capitalOutSucList">成功提现人民币列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalInList.html">
                    <li><a href="ssadmin/capitalInList.html" target="navTab"
                           rel="capitalInList">待审核人民币充值列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalOutList1.html">
                    <li><a href="ssadmin/capitalOutList1.html" target="navTab"
                           rel="capitalOutList1">待审核人民币提现列表(初审)</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalOutList2.html">
                    <li><a href="ssadmin/capitalOutList2.html" target="navTab"
                           rel="capitalOutList2">待审核人民币提现列表(复审)</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitalOutList3.html">
                    <li><a href="ssadmin/capitalOutList3.html" target="navTab"
                           rel="capitalOutList3">待审核人民币提现列表(终审)</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/walletList.html">
                    <li><a href="ssadmin/walletList.html" target="navTab"
                           rel="walletList">会员人民币列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/operationLogList.html">
                    <li><a href="ssadmin/operationLogList.html" target="navTab"
                           rel="operationLogList">人民币手工充值列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/withdrawFeesList.html">
                    <li><a href="ssadmin/withdrawFeesList.html" target="navTab"
                           rel="withdrawFeesList" title="人民币提现手续费列表">人民币提现手续费列表</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="report">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>报表统计
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/otcfeeList.html" target="navTab"
                           rel="otcfeeList">OTC手续费交易明细表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/feeList.html" target="navTab"
                           rel="feeList">币币交易手续费明细表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/withdrawFeeList.html" target="navTab"
                           rel="withdrawFeeList">提现手续费明细表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/userReport.html" target="navTab"
                           rel="userReport">会员注册统计表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitaloperationReport.html">
                    <li><a
                            href="ssadmin/capitaloperationReport.html?type=1&status=3&url=ssadmin/capitaloperationReport"
                            target="navTab" rel="capitaloperationReport">人民币充值统计表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/capitaloperationOutReport.html">
                    <li><a
                            href="ssadmin/capitaloperationReport.html?type=2&status=3&url=ssadmin/capitaloperationOutReport"
                            target="navTab" rel="capitaloperationOutReport">人民币提现统计表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/vcOperationInReport.html">
                    <li><a
                            href="ssadmin/vcOperationReport.html?type=1&status=3&url=ssadmin/vcOperationInReport"
                            target="navTab" rel="vcOperationInReport">虚拟币充值统计表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
                    <li><a
                            href="ssadmin/vcOperationReport.html?type=2&status=3&url=ssadmin/vcOperationOutReport"
                            target="navTab" rel="vcOperationOutReport">虚拟币提现统计表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
                    <li><a
                            href="ssadmin/dailyReconciliation.html?ftype=0"
                            target="navTab" rel="dailyReconciliation">每日财务对账表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
                    <li><a
                            href="ssadmin/transferRecord.html?ftype=0"
                            target="navTab" rel="transferRecord">平台钱包转账记录</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
                    <li><a href="ssadmin/queryCoinNumber.html"
                           target="navTab" rel="queryCoinNumberList">人员持币量列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/totalReport.html">
                    <li><a href="ssadmin/totalReport.html" target="navTab"
                           rel="totalReport">综合统计表</a>
                    </li>
                    <!-- <li><a href="ssadmin/userWalletReport.html" target="navTab"
                    rel="userWalletReport">盈利统计表</a>
                    </li> -->
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/UserReconciliationRecordlists.html" target="navTab"
                           rel="UserReconciliationRecordlists">会员对账信息表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/userReport.html">
                    <li><a href="ssadmin/UserOverflowTracing.html" target="navTab"
                           rel="UserOverflowTracing">资金溢出追查表</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="question">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>提问管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/questionList.html">
                    <li><a href="ssadmin/questionList.html" target="navTab"
                           rel="questionList">提问记录列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/questionForAnswerList.html">
                    <li><a href="ssadmin/questionForAnswerList.html"
                           target="navTab" rel="questionList">待回复提问列表</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="vote">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>新币投票管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="/ssadmin/coinVoteList.html">
                    <li><a href="/ssadmin/coinVoteList.html" target="navTab"
                           rel="coinVoteList">投票信息列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="/ssadmin/coinVoteLogList.html">
                    <li><a href="/ssadmin/coinVoteLogList.html" target="navTab"
                           rel="coinVoteLogList">投票记录列表</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

    <shiro:hasPermission name="system">
        <div class="accordionHeader">
            <h2>
                <span>Folder</span>系统管理
            </h2>
        </div>
        <div class="accordionContent">
            <ul class="tree treeFolder">
                <shiro:hasPermission name="ssadmin/systemArgsList.html">
                    <li><a href="ssadmin/systemArgsList.html" target="navTab"
                           rel="systemArgsList">系统参数列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/systemArgsList.html">
                    <li><a href="ssadmin/candlestickChart.html" target="navTab"
                           rel="systemArgsList">K线图数据参数列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/linkList.html">
                    <li><a href="ssadmin/linkList.html" target="navTab"
                           rel="linkList">友情链接列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/systemBankList.html">
                    <li><a href="ssadmin/systemBankList.html" target="navTab"
                           rel="systemBankList">银行帐户列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/aboutList.html">
                    <li><a href="ssadmin/aboutList.html" target="navTab"
                           rel="aboutList">帮助分类列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/securityList.html">
                    <li><a
                            href="ssadmin/goSecurityJSP.html?url=ssadmin/securityTreeList&treeId=1"
                            target="navTab" rel="securityTreeList">权限列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/roleList.html">
                    <li><a href="ssadmin/roleList.html" target="navTab"
                           rel="roleList">角色列表</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/adminList.html">
                    <li><a href="ssadmin/adminList.html" target="navTab"
                           rel="adminList">管理员列表</a>
                    </li>
                    <li><a href="ssadmin/countLimitList.html" target="navTab"
                           rel="countLimitList">限制管理列表</a>
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="ssadmin/limittradeList.html">
                    <li><a href="ssadmin/limittradeList.html" target="navTab"
                           rel="limittradeList">限价交易列表</a>
                    </li>
                    <li><a href="ssadmin/autotradeList.html" target="navTab"
                           rel="autotradeList">自动交易列表</a>
                    </li>
                    <li><a href="ssadmin/querySecurityCode.html" target="navTab"
                           rel="securityCodeList">短信验证码列表</a>
                    </li>
                    <li><a href="ssadmin/appdownload.html" target="navTab"
                           rel="appdownload">版本上传列表</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </shiro:hasPermission>

</div>