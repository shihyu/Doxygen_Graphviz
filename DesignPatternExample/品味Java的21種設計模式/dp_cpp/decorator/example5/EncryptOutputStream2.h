#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example5
				{


					///
					/// <summary> * ʵ�ּ򵥵ļ��� </summary>
					/// 
					class EncryptOutputStream2 : public java::io::FilterOutputStream
					{
					public:
						EncryptOutputStream2(OutputStream *os);

						virtual void write(int a) throw(IOException);
					};

				}
			}
		}
	}
}