#include "DepManager.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example5
				{

					object *DepManager::handleRequest(RequestModel *rm)
					{
						if(FeeRequestModel::FEE_TYPE.equals(rm->getType()))
						{
							return handleFeeRequest(rm);
						}
						else
						{
							return Handler::handleRequest(rm);
						}
					}

					object *DepManager::handleFeeRequest(RequestModel *rm)
					{
						//先把通用的对象造型回来
						FeeRequestModel *frm = static_cast<FeeRequestModel*>(rm);
						std::string str = "";
						//部门经理的权限只能在1000以内
						if(frm->getFee() < 1000)
						{
							//为了测试，简单点，只同意小李申请的
							if((new std::string("小李"))->equals(frm->getUser()))
							{
								str = "部门经理同意"+frm->getUser()+"聚餐费用"+frm->getFee()+"元的请求";
							}
							else
							{
								//其他人一律不同意
								str = "部门经理不同意"+frm->getUser()+"聚餐费用"+frm->getFee()+"元的请求";
							}
							return str;
						}
						else
						{
							//超过1000，继续传递给级别更高的人处理
							if(this->successor!=0)
							{
								return this->successor->handleRequest(rm);
							}
						}
						return str;
					}
				}
			}
		}
	}
}
