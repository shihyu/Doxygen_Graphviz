#pragma once

#include "C1.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example6
				{

					class A1
					{
					///	
					///	 <summary> * ��������������C1�������ڴ�����ע�������;�� </summary>
					///	 * <returns> C1�Ķ���ʵ�� </returns>
					///	 
					protected:
						virtual C1 *createC1() = 0;
					public:
						virtual void t1();
					};

				}
			}
		}
	}
}