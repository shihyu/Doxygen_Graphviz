#pragma once

#include "Colleague.h"
#include "Mediator.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example2
				{
					///
					/// <summary> * �Կ��࣬һ��ͬ���� </summary>
					/// 
					class VideoCard : public Colleague
					{
					public:
						VideoCard(Mediator *mediator);

					///	
					///	 <summary> * ��ʾ��Ƶ���� </summary>
					///	 * <param name="data"> ����ʾ������ </param>
					///	 
						virtual void showData(std::string data);

					};

				}
			}
		}
	}
}