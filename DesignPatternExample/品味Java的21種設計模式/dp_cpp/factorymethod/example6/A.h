#pragma once

#include "C.h"

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

					class A
					{
					///	
					///	 <summary> * �ȴ���ע����� </summary>
					///	 
					private:
						C *c;
					///	
					///	 <summary> * ע����ԴC�ķ��� </summary>
					///	 * <param name="c"> ��ע�����Դ </param>
					///	 
					public:
						virtual void setC(C *c);
						virtual void t1();
					};

				}
			}
		}
	}
}