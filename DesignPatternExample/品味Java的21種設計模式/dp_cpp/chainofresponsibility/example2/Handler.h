#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example2
				{
					///
					/// <summary> * ְ��Ľӿڣ�Ҳ���Ǵ�������Ľӿ� </summary>
					/// 
					class Handler
					{
					///	
					///	 <summary> * ���к�̵�ְ����� </summary>
					///	 
					protected:
						Handler *successor;
					///	
					///	 <summary> * ���ú�̵�ְ����� </summary>
					///	 * <param name="successor"> ��̵�ְ����� </param>
					///	 
					public:
						virtual void setSuccessor(Handler *successor);
					///	
					///	 <summary> * ʾ�⴦������ķ�������Ȼ���ʾ�ⷽ����û�д��������
					///	 * ��ʵ���ǿ��Դ�������ģ����ݾ�����Ҫ��ѡ���Ƿ񴫵ݲ��� </summary>
					///	 
						virtual void handleRequest() = 0;
					};


				}
			}
		}
	}
}