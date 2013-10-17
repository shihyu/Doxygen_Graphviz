#include "ProjectManager.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example3
				{

					std::string ProjectManager::handleFeeRequest(std::string user, double fee)
					{
						std::string str = "";
						//项目经理的权限比较小，只能在500以内
						if(fee < 500)
						{
							//为了测试，简单点，只同意小李的
							if((new std::string("小李"))->equals(user))
							{
								str = "项目经理同意"+user+"聚餐费用"+fee+"元的请求";
							}
							else
							{
								//其他人一律不同意
								str = "项目经理不同意"+user+"聚餐费用"+fee+"元的请求";
							}
							return str;
						}
						else
						{
							//超过500，继续传递给级别更高的人处理
							if(this->successor!=0)
							{
								return successor->handleFeeRequest(user, fee);
							}
						}
						return str;
					}
				}
			}
		}
	}
}
