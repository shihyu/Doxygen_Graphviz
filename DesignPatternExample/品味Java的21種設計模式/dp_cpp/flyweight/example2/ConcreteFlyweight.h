#pragma once

#include "Flyweight.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example2
				{
					///
					/// <summary> * ��Ԫ���� </summary>
					/// 
					class ConcreteFlyweight : public Flyweight
					{
					///	
					///	 <summary> * ʾ���������ڲ�״̬ </summary>
					///	 
					private:
						std::string intrinsicState;
					///	
					///	 <summary> * ���췽����������Ԫ������ڲ�״̬������ </summary>
					///	 * <param name="state"> ��Ԫ������ڲ�״̬������ </param>
					///	 
					public:
						ConcreteFlyweight(std::string state);
						virtual void operation(std::string extrinsicState);
					};

				}
			}
		}
	}
}