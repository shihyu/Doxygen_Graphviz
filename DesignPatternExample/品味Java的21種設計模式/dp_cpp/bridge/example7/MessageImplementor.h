#pragma once

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
					/// <summary> * ʵ�ַ�����Ϣ��ͳһ�ӿ� </summary>
					/// 
					class MessageImplementor
					{
					///	
					///	 <summary> * ������Ϣ </summary>
					///	 * <param name="message"> Ҫ���͵���Ϣ���� </param>
					///	 * <param name="toUser"> ����Ϣ���͵�Ŀ����Ա </param>
					///	 
					public:
						virtual public void send(std::string, std::string) = 0;
					};

				}
			}
		}
	}
}