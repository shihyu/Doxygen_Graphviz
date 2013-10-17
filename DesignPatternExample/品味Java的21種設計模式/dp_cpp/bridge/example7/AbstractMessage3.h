#pragma once

#include "MessageImplementor.h"
#include "MessageSMS.h"
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
					/// <summary> * �������Ϣ���� </summary>
					/// 
					class AbstractMessage3
					{
					///	
					///	 <summary> * ����һ��ʵ�ֲ��ֵĶ��� </summary>
					///	 
					protected:
						MessageImplementor *impl;
					///	
					///	 <summary> * ���췽�� </summary>
					///	 
					public:
						AbstractMessage3();

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