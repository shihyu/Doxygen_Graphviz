#pragma once

#include "AbstractClass.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace strategy
			{
				namespace example7
				{

					///
					/// <summary> * ʵ�־�����㷨 </summary>
					/// 
					class ConcreteStrategyC : public AbstractClass
					{
					protected:
						virtual void stepOneOpe();
						virtual void stepTwoOpe();
					};



				}
			}
		}
	}
}