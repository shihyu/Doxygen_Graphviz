#pragma once
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example2
				{
					///
					/// <summary> * �ӿڵĶ��壬�ýӿڿ���ͨ���򵥹��������� </summary>
					/// 
					class Api
					{
					///	
					///	 <summary> * ʾ�⣬����Ĺ��ܷ����Ķ��� </summary>
					///	 * <param name="s"> ʾ�⣬��Ҫ�Ĳ��� </param>
					///	 
					public:
						virtual void operation(std::string) = 0;
					};

				}
			}
		}
	}
}
