#pragma once

#include "Implementor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example5
				{

					///
					/// <summary> * �����ľ���ʵ�ֶ��� </summary>
					/// 
					class ConcreteImplementorB : public Implementor
					{
					public:
						virtual void operationImpl();
					};
				}
			}
		}
	}
}