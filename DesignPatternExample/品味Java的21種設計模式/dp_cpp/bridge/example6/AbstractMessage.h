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
				namespace example6
				{

					///
					/// <summary> * �������Ϣ���� </summary>
					/// 
					class AbstractMessage
					{
					///	
					///	 <summary> * ����һ��ʵ�ֲ��ֵĶ��� </summary>
					///	 
					protected:
						MessageImplementor *impl;
					///	
					///	 <summary> * ���췽��������ʵ�ֲ��ֵĶ���  </summary>
					///	 * <param name="impl"> ʵ�ֲ��ֵĶ��� </param>
					///	 
					public:
						AbstractMessage(MessageImplementor *impl);

					///	
					///	 <summary> * ������Ϣ��ת��ʵ�ֲ��ֵķ��� </summary>
					///	 * <param name="message"> Ҫ���͵���Ϣ���� </param>
					///	 * <param name="toUser"> ����Ϣ���͵�Ŀ����Ա </param>
					///	 
						virtual void sendMessage(std::string message, std::string toUser);

					};

				}
			}
		}
	}
}