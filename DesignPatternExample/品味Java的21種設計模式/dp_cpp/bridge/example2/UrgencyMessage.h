#pragma once

#include "Message.h"

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
					///
					/// <summary> * �Ӽ���Ϣ�ĳ���ӿ� </summary>
					/// 
					class UrgencyMessage : public Message
					{
					///	
					///	 <summary> * ���ĳ��Ϣ�Ĵ������ </summary>
					///	 * <param name="messageId"> ����ص���Ϣ�ı�� </param>
					///	 * <returns> ������ص������ݶ�������ʾ��һ�£���������Object </returns>
					///	 
					public:
						virtual public object *watch(std::string) = 0;
					};

				}
			}
		}
	}
}