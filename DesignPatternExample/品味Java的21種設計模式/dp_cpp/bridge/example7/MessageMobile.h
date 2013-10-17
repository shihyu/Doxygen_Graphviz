#pragma once

#include "MessageImplementor.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example7
				{
					///
					/// <summary> * ���ֻ�����Ϣ�ķ�ʽ������Ϣ </summary>
					/// 
					class MessageMobile : public MessageImplementor
					{

					public:
						virtual void send(std::string message, std::string toUser);
					};

				}
			}
		}
	}
}