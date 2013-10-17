#pragma once

#include "MessageImplementor.h"
#include "MessageSMS.h"
#include "MessageMobile.h"
#include "MessageEmail.h"
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
					class AbstractMessage2
					{
					///	
					///	 <summary> * ������Ϣ�ĳ�����ѡ����ʵ�ʵ�� </summary>
					///	 * <param name="message"> Ҫ���͵���Ϣ </param>
					///	 * <returns> ���ʵ�ʵ�ֶ��� </returns>
					///	 
					protected:
						virtual MessageImplementor *getImpl(std::string message);

					///	
					///	 <summary> * ���췽�� </summary>
					///	 
					public:
						AbstractMessage2();

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