#pragma once

#include "A.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example2
				{

					class C2
					{
					///	
					///	 <summary> * ����A�����ʵ�� </summary>
					///	 
					private:
						A *a;

					public:
						virtual void a1();
						virtual void c11();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								a = new A();

								initialized = true;
							}
						}

public:
	C2()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}