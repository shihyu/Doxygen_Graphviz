#pragma once
#include <iostream>

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
					/// <summary> * ��Ϣ��ͳһ�ӿ� </summary>
					/// 
					class Message
					{
					///	
					///	 <summary> * ������Ϣ </summary>
					///	 * <param name="message"> Ҫ���͵���Ϣ���� </param>
					///	 * <param name="toUser"> ����Ϣ���͵�Ŀ����Ա </param>
					///	 
					public:
						virtual void send(std::string, std::string) = 0;
					};

				}
			}
		}
	}
}
