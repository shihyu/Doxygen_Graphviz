#pragma once

#include "A.h"
#include "B.h"

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

					class C4
					{
						//ʾ��ֱ���������ϴ�����Ҫ��ϵĶ���
					private:
						A *a;
						//ʾ��ͨ���ⲿ������Ҫ��ϵĶ���
						B *b;
					public:
						virtual void setB(B *b);
						virtual void a1();
						virtual void b1();
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
	C4()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}