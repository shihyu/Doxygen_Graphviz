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
					/// <summary> * �����࣬һ��ͬ���� </summary>
					/// 
					class SoundCard : public Colleague
					{
					public:
						SoundCard(Mediator *mediator);

					///	
					///	 <summary> * ������Ƶ���ݷ������� </summary>
					///	 * <param name="data"> �������������� </param>
					///	 
						virtual void soundData(std::string data);

					};

				}
			}
		}
	}
}