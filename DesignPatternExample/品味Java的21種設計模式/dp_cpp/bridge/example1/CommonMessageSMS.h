#pragma once

#include "Message.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example1
				{
					///
					/// <summary> * ��վ�ڶ���Ϣ�ķ�ʽ������ͨ��Ϣ </summary>
					/// 
					class CommonMessageSMS : public Message
					{

					public:
						virtual void send(std::string message, std::string toUser);

					};

				}
			}
		}
	}
}