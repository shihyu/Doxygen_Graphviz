#pragma once

#include "AbstractMessage.h"
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
				namespace example6
				{
					///
					/// <summary> * �Ӽ���Ϣ </summary>
					/// 
					class UrgencyMessage : public AbstractMessage
					{
					public:
						UrgencyMessage(MessageImplementor *impl);
						virtual void sendMessage(std::string message, std::string toUser);
					///	
					///	 <summary> * ���ĳ��Ϣ�Ĵ������ </summary>
					///	 * <param name="messageId"> ����ص���Ϣ�ı�� </param>
					///	 * <returns> ������ص������ݶ�������ʾ��һ�£���������Object </returns>
					///	 
						virtual object *watch(std::string messageId);
					};
				}
			}
		}
	}
}