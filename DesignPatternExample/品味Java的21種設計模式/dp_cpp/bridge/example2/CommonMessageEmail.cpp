#include "CommonMessageEmail.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example2
				{

					void CommonMessageEmail::send(std::string message, std::string toUser)
					{
						puts("使用Email的方式，发送消息'"+message+"'给"+toUser);
					}
				}
			}
		}
	}
}
